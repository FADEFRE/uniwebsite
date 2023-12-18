# development
FROM public.ecr.aws/docker/library/node:lts-alpine as development
WORKDIR /app
COPY package*.json ./ 
RUN npm install 
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host"]

# build
FROM development as build
WORKDIR /app
ARG VITE_URL
ENV VITE_URL=http://172.26.92.91:8090
COPY . .
RUN npm run build

# production
FROM nginx:alpine as production
WORKDIR /app
COPY --from=build /app/dist /app
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
