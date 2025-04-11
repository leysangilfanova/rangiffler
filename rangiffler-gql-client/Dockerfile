FROM node:20.14.0-slim AS build
ARG NPM_COMMAND
ARG VERSION
WORKDIR /app

COPY package*.json ./
RUN npm install --force  # Added --force flag
COPY . ./
RUN npm run ${NPM_COMMAND}
 
# release step
FROM nginx:1.27.1-alpine AS release
LABEL maintainer="Irina Stiazhkina @irin_alexis"
LABEL version=${VERSION}
ENV TZ=Europe/Moscow
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html/
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]