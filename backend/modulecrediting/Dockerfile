# development
FROM public.ecr.aws/docker/library/alpine:latest as development
WORKDIR /app
COPY pom.xml ./ 
EXPOSE 8090

# build
FROM development as build
RUN apk --no-cache add maven openjdk17
COPY . .
RUN mvn clean package

# production
FROM public.ecr.aws/docker/library/alpine:latest as production
EXPOSE 8090
RUN apk --no-cache add maven openjdk17
RUN mkdir -p app/target
COPY --from=build /app/target /app/target