FROM node:alpine as builder

WORKDIR /home

COPY ./package.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
EXPOSE 3000

COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /home/build /usr/share/nginx/html