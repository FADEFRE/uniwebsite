# development
FROM public.ecr.aws/docker/library/node:lts-alpine as development
WORKDIR /app
COPY package*.json ./ 
RUN npm install 
EXPOSE 8080
CMD ["npm", "run", "serve"]

# build
FROM development as build
COPY . .
RUN npm run build

# production
FROM public.ecr.aws/docker/library/nginx:alpine as production
COPY --from=build /app/dist /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]