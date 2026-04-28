FROM maven:3.9-eclipse-temurin-25

# Crear usuario no root
RUN groupadd -r nonroot && useradd -r -g nonroot -m nonroot \
    && mkdir -p /webapp \
    && chown -R nonroot:nonroot /webapp

WORKDIR /webapp

# Copiar código
COPY . .

# Compilar proyecto
RUN mvn clean package -DskipTests

# Crear carpeta .m2 para evitar errores de permisos
RUN mkdir -p /home/nonroot/.m2 \
    && chown -R nonroot:nonroot /home/nonroot

# Ajustar permisos del proyecto
RUN chown -R nonroot:nonroot /webapp

# Cambiar a usuario no root
USER nonroot

# Variables de entorno correctas
ENV HOME=/home/nonroot
ENV MAVEN_CONFIG=/home/nonroot/.m2

CMD ["mvn", "spring-boot:run"]