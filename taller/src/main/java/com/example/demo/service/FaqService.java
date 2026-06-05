package com.example.demo.service;

import com.example.demo.dto.FaqChatResponse;
import com.example.demo.faq.PrivacidadFaqTool;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaqService {

    private static final Logger logger = LogManager.getLogger(FaqService.class);

    private final ChatClient chatClient;
    private final PrivacidadFaqTool privacidadFaqTool;

    private static final String SYSTEM_PROMPT = """
            Eres el asistente virtual de EventApp, una plataforma de reserva de entradas para eventos.
            Tu único propósito es responder preguntas sobre la política de privacidad de EventApp.
            
            Reglas de idioma:
            - Detecta automáticamente el idioma del usuario.
            - Responde siempre en el mismo idioma que el usuario.
            - Si no está claro, responde en español.
            
            Reglas de tono:
            - Amable, conciso y profesional.
            - Respuestas cortas: máximo 3-4 frases.
            - Solo texto plano, sin markdown.
            
            Reglas de alcance:
            - SOLO responde preguntas sobre:
              1. Política de privacidad de EventApp
              2. Cancelación de reservas
              3. Cancelación de eventos
              4. Reembolsos
              5. Seguridad y protección de datos
              6. Cookies
              7. Derechos del usuario (RGPD)
              8. Datos que recoge EventApp y cómo los usa
              9. Contacto con EventApp
            
            Reglas de herramientas:
            - Si la pregunta es sobre alguno de los temas anteriores,
              DEBES llamar a la herramienta privacidad_faq con los intents correspondientes.
            - Nunca inventes políticas ni datos.
            - Si la pregunta está fuera del alcance, responde:
              "Solo puedo responder preguntas sobre la política de privacidad de EventApp.
               Para más información visita la sección de Política de Privacidad en nuestra web."
            
            Recuerda: nunca menciones las herramientas ni los intents en tu respuesta.
            """;

    public FaqChatResponse getAnswer(String question, String conversationId) {
        try {
            ChatResponse response = chatClient
                    .prompt()
                    .system(SYSTEM_PROMPT)
                    .user(question)
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                    .tools(privacidadFaqTool)
                    .call()
                    .chatResponse();

            String answer = response.getResult().getOutput().getText();

            var usage = response.getMetadata().getUsage();
            Integer prompt = usage != null ? usage.getPromptTokens() : null;
            Integer completion = usage != null ? usage.getCompletionTokens() : null;
            Integer total = usage != null ? usage.getTotalTokens() : null;

            logger.info("FAQ tokens | prompt={} completion={} total={}",
                    prompt, completion, total);

            return new FaqChatResponse(answer,
                    new FaqChatResponse.TokenUsage(prompt, completion, total));

        } catch (Exception ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Error desconocido";

            if (msg.contains("RESOURCE_EXHAUSTED") || msg.contains("429")
                    || msg.contains("Quota")) {
                logger.warn("Gemini quota exceeded: {}", msg);
                return new FaqChatResponse(
                        "El asistente ha alcanzado su límite de uso por hoy. " +
                                "Por favor, consulta directamente la Política de Privacidad " +
                                "en nuestra web.", null);
            }

            logger.error("Error en FaqService", ex);
            return new FaqChatResponse(
                    "Ha ocurrido un error. Por favor, consulta la Política de " +
                            "Privacidad directamente en nuestra web.", null);
        }
    }
}