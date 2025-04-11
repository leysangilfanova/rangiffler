# Stage 1: Schema composition
FROM node:18-bookworm-slim AS composer
WORKDIR /app

RUN apt-get update && \
    apt-get install -y curl ca-certificates && \
    rm -rf /var/lib/apt/lists/*

# Install specific Rover version
ENV ROVER_VERSION=0.18.0
RUN curl -sL https://rover.apollo.dev/nix/v${ROVER_VERSION} | sh && \
    mv /root/.rover/bin/rover /usr/local/bin/

COPY supergraph.yaml .
COPY router.yaml .
COPY subgraphs/ ./subgraphs/

# Verify file structure
RUN ls -la subgraphs/*.graphql

RUN rover supergraph compose \
  --config ./supergraph.yaml \
  --output ./supergraph.graphql \
  --elv2-license accept

# Stage 2: Router
FROM ghcr.io/apollographql/router:v2.1.2-rc.0
COPY --from=composer /app/router.yaml /config/
COPY --from=composer /app/supergraph.graphql /config/

EXPOSE 4000
CMD ["-c", "/config/router.yaml", "-s", "/config/supergraph.graphql", "--log", "debug", "--dev"]