package com.example.demo.faq;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PrivacidadFaqTool {

    private final PrivacidadFaqRepository repository;

    public PrivacidadFaqTool(PrivacidadFaqRepository repository) {
        this.repository = repository;
    }

    @Tool(
            name = "privacidad_faq",
            description = """
                    Usa esta herramienta para responder preguntas sobre la política
                    de privacidad de EventApp.
                    Intents disponibles:
                    DATOS_RECOGIDOS, USO_DATOS, CANCELACION_RESERVA, CANCELACION_EVENTO,
                    REEMBOLSO, SEGURIDAD, COOKIES, DERECHOS_USUARIO, CONTACTO, GENERAL.
                    Llama a esta herramienta SIEMPRE que la pregunta sea sobre privacidad,
                    cancelaciones, reembolsos, seguridad, cookies o derechos del usuario.
                    """
    )
    public Map<PrivacidadFaqIntent, String> privacidadFaq(
            @ToolParam(description = "Lista de intents detectados en la pregunta")
            List<PrivacidadFaqIntent> intents
    ) {
        return repository.findAnswers(intents);
    }
}