package com.example.demo.service;

import com.example.demo.dto.EmailDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired private JavaMailSender mailSender;

    public void enviarRecordatorio(String destinatario, EmailDTO notif) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom("noreply@eventapp.es");
            helper.setTo(destinatario);
            helper.setSubject("🔔 Recordatorio: " + notif.getNombreEvento()
                    + " es mañana");

            String html = """
                    <div style="font-family:Arial,sans-serif;max-width:600px;margin:0 auto">
                        <div style="background:linear-gradient(135deg,#2c3e50,#e94560);
                                    padding:32px;border-radius:12px 12px 0 0;text-align:center">
                            <h1 style="color:white;margin:0;font-size:1.8rem">
                                Event<span style="color:#ffd6de">App</span>
                            </h1>
                        </div>
                        <div style="background:white;padding:32px;border-radius:0 0 12px 12px;
                                    box-shadow:0 4px 16px rgba(0,0,0,0.1)">
                            <h2 style="color:#2c3e50">🔔 Recordatorio de evento</h2>
                            <p style="color:#636e72">Tu evento está próximo. ¡No te lo pierdas!</p>
                            <div style="background:#f5f6fa;border-left:4px solid #e94560;
                                        border-radius:8px;padding:20px;margin:20px 0">
                                <h3 style="color:#2c3e50;margin:0 0 12px 0">
                                    %s
                                </h3>
                                <p style="margin:4px 0;color:#636e72">
                                    📅 <strong>%s</strong>
                                </p>
                                <p style="margin:4px 0;color:#636e72">
                                    📍 <strong>%s</strong>
                                </p>
                                <p style="margin:4px 0;color:#636e72;font-family:monospace">
                                    Localizador: <strong>%s</strong>
                                </p>
                            </div>
                            <div style="text-align:center;margin-top:24px">
                                <a href="http://localhost:8085/usuario/reservas"
                                   style="background:#e94560;color:white;padding:12px 32px;
                                          border-radius:8px;text-decoration:none;
                                          font-weight:bold;display:inline-block">
                                    Ver mis reservas
                                </a>
                            </div>
                            <hr style="border-color:#dfe6e9;margin:24px 0">
                            <p style="color:#b2bec3;font-size:0.8rem;text-align:center">
                                © 2025 EventApp ·
                                <a href="http://localhost:8085/politicaPrivacidad"
                                   style="color:#b2bec3">Política de privacidad</a>
                            </p>
                        </div>
                    </div>
                    """.formatted(
                    notif.getNombreEvento(),
                    notif.getFecha(),
                    notif.getLugar(),
                    notif.getLocalizador()
            );

            helper.setText(html, true);
            mailSender.send(mensaje);

        } catch (Exception e) {
            System.err.println("[EMAIL] Error enviando recordatorio a "
                    + destinatario + ": " + e.getMessage());
        }
    }

    public void enviarConfirmacionReserva(String destinatario,
                                          String nombreEvento,
                                          String fecha,
                                          String lugar,
                                          String localizador,
                                          Integer numEntradas,
                                          String precioTotal) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom("noreply@eventapp.es");
            helper.setTo(destinatario);
            helper.setSubject("✅ Reserva confirmada — " + nombreEvento);

            String html = """
                    <div style="font-family:Arial,sans-serif;max-width:600px;margin:0 auto">
                        <div style="background:linear-gradient(135deg,#2c3e50,#e94560);
                                    padding:32px;border-radius:12px 12px 0 0;text-align:center">
                            <h1 style="color:white;margin:0;font-size:1.8rem">
                                Event<span style="color:#ffd6de">App</span>
                            </h1>
                        </div>
                        <div style="background:white;padding:32px;border-radius:0 0 12px 12px;
                                    box-shadow:0 4px 16px rgba(0,0,0,0.1)">
                            <div style="text-align:center;margin-bottom:24px">
                                <div style="width:64px;height:64px;background:linear-gradient(135deg,#28a745,#20c997);
                                            border-radius:50%%;display:inline-flex;align-items:center;
                                            justify-content:center;font-size:2rem">✓</div>
                                <h2 style="color:#2c3e50;margin:12px 0 4px">¡Reserva confirmada!</h2>
                                <p style="color:#636e72;margin:0">
                                    Tu reserva ha sido procesada correctamente.
                                </p>
                            </div>
                            <div style="background:#f5f6fa;border-left:4px solid #e94560;
                                        border-radius:8px;padding:20px;margin:20px 0">
                                <h3 style="color:#2c3e50;margin:0 0 16px 0">%s</h3>
                                <table style="width:100%%;color:#636e72">
                                    <tr>
                                        <td style="padding:4px 0">📅 Fecha</td>
                                        <td style="text-align:right;font-weight:bold;color:#2c3e50">%s</td>
                                    </tr>
                                    <tr>
                                        <td style="padding:4px 0">📍 Lugar</td>
                                        <td style="text-align:right;font-weight:bold;color:#2c3e50">%s</td>
                                    </tr>
                                    <tr>
                                        <td style="padding:4px 0">🎟️ Entradas</td>
                                        <td style="text-align:right;font-weight:bold;color:#2c3e50">%s</td>
                                    </tr>
                                    <tr>
                                        <td style="padding:4px 0;border-top:1px solid #dfe6e9">💶 Total</td>
                                        <td style="text-align:right;font-weight:bold;color:#e94560;
                                                   border-top:1px solid #dfe6e9;font-size:1.2rem">%s €</td>
                                    </tr>
                                </table>
                                <p style="margin:16px 0 0;color:#b2bec3;font-family:monospace;font-size:0.85rem">
                                    Localizador: <strong style="color:#2c3e50">%s</strong>
                                </p>
                            </div>
                            <div style="text-align:center;margin-top:24px">
                                <a href="http://localhost:8085/usuario/reservas"
                                   style="background:#2c3e50;color:white;padding:12px 32px;
                                          border-radius:8px;text-decoration:none;
                                          font-weight:bold;display:inline-block">
                                    Ver mis reservas
                                </a>
                            </div>
                            <hr style="border-color:#dfe6e9;margin:24px 0">
                            <p style="color:#b2bec3;font-size:0.8rem;text-align:center">
                                © 2025 EventApp ·
                                <a href="http://localhost:8085/politicaPrivacidad"
                                   style="color:#b2bec3">Política de privacidad</a>
                            </p>
                        </div>
                    </div>
                    """.formatted(
                    nombreEvento, fecha, lugar,
                    numEntradas, precioTotal, localizador
            );

            helper.setText(html, true);
            mailSender.send(mensaje);

        } catch (Exception e) {
            System.err.println("[EMAIL] Error enviando confirmación a "
                    + destinatario + ": " + e.getMessage());
        }
    }
}