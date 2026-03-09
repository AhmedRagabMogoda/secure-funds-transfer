# ============================================================
# Stage 1: Build
# Uses Node.js to install dependencies and compile Angular
# ============================================================
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files first to leverage Docker layer cache
COPY package*.json ./
RUN npm ci --legacy-peer-deps

# Copy source and build for production
COPY . .
RUN npm run build:prod

# ============================================================
# Stage 2: Runtime
# Serves the compiled Angular app via Nginx
# ============================================================
FROM nginx:1.25-alpine

# Remove default nginx static content
RUN rm -rf /usr/share/nginx/html/*

# Copy compiled Angular output from builder stage
COPY --from=builder /app/dist/transfer-app-frontend /usr/share/nginx/html

# Copy custom nginx config (handles Angular routing + API proxy)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
