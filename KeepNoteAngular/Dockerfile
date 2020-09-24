FROM node:8.10.0
WORKDIR usr/lib/app
COPY dist dist 
RUN npm install http-server -g
ENTRYPOINT ["http-server", "dist/keep3"]
