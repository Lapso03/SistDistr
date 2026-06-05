package com.example.demo.faq;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PrivacidadFaqRepository {

    private final Map<PrivacidadFaqIntent, String> data = new EnumMap<>(PrivacidadFaqIntent.class);

    public PrivacidadFaqRepository() {
        data.put(PrivacidadFaqIntent.DATOS_RECOGIDOS,
                "EventApp recoge únicamente: nombre de usuario, contraseña cifrada con BCrypt, " +
                        "email (opcional), datos de reserva (evento, entradas, fecha, precio) y preferencia " +
                        "de recordatorio. No se recogen ni almacenan datos bancarios reales.");

        data.put(PrivacidadFaqIntent.USO_DATOS,
                "Los datos se usan exclusivamente para: gestionar tu cuenta, procesar reservas, " +
                        "enviar notificaciones de recordatorio si las solicitas, y garantizar el " +
                        "funcionamiento de la plataforma. No se ceden a terceros ni se usan con fines " +
                        "publicitarios.");

        data.put(PrivacidadFaqIntent.CANCELACION_RESERVA,
                "Puedes cancelar una reserva gratuitamente hasta 48 horas antes del evento. " +
                        "Las cancelaciones en las 48h previas no tienen derecho a reembolso salvo " +
                        "cancelación del evento por el organizador. Las reservas en estado PENDIENTE " +
                        "pueden cancelarse en cualquier momento sin penalización.");

        data.put(PrivacidadFaqIntent.CANCELACION_EVENTO,
                "Si un organizador cancela un evento, todas las reservas activas se cancelan " +
                        "automáticamente y los usuarios reciben reembolso íntegro. También aplica " +
                        "reembolso completo en cambios sustanciales de fecha/lugar (más de 24h de " +
                        "diferencia o cambio de ciudad) y en casos de fuerza mayor.");

        data.put(PrivacidadFaqIntent.REEMBOLSO,
                "El reembolso es íntegro cuando: el organizador cancela el evento, hay cambios " +
                        "sustanciales en fecha u hora, o hay causas de fuerza mayor. " +
                        "Al ser una plataforma de demostración los pagos son simulados. En producción " +
                        "el reembolso se procesaría en 5-10 días hábiles por el mismo método de pago.");

        data.put(PrivacidadFaqIntent.SEGURIDAD,
                "EventApp implementa: contraseñas cifradas con BCrypt, control de acceso por " +
                        "roles (USER, ORGANIZADOR, ADMIN), y sesiones gestionadas de forma segura " +
                        "con Spring Security.");

        data.put(PrivacidadFaqIntent.COOKIES,
                "EventApp usa únicamente cookies de sesión técnicas necesarias: JSESSIONID " +
                        "para mantener al usuario autenticado, que se elimina al cerrar el navegador. " +
                        "No se usan cookies de seguimiento, publicidad ni análisis de terceros.");

        data.put(PrivacidadFaqIntent.DERECHOS_USUARIO,
                "Tienes derecho a: acceso (conocer tus datos), rectificación (corregir datos " +
                        "inexactos), supresión (eliminar tus datos) y portabilidad (recibir tus datos " +
                        "en formato estructurado). Para ejercerlos contacta en info@eventapp.es.");

        data.put(PrivacidadFaqIntent.CONTACTO,
                "Puedes contactar con EventApp en: " +
                        "📍 Av. de Cantabria, s/n, 09006 Burgos · " +
                        "✉️ info@eventapp.es · " +
                        "GitHub: github.com/Lapso03/SistDistr");

        data.put(PrivacidadFaqIntent.GENERAL,
                "La política de privacidad de EventApp cumple con el RGPD y la LOPDGDD. " +
                        "Puedes consultarla completa en /politicaPrivacidad. " +
                        "Incluye secciones sobre datos recogidos, uso, cancelaciones, seguridad, " +
                        "cookies, derechos y contacto.");
    }

    public Map<PrivacidadFaqIntent, String> findAnswers(List<PrivacidadFaqIntent> intents) {
        return intents.stream()
                .filter(data::containsKey)
                .collect(Collectors.toMap(i -> i, data::get));
    }

    public String findAnswer(PrivacidadFaqIntent intent) {
        return data.getOrDefault(intent,
                "No tengo información específica sobre eso. Consulta la política " +
                        "de privacidad completa en /politicaPrivacidad.");
    }
}