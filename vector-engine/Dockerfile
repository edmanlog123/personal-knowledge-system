# ---------- BUILD STAGE ----------
    FROM gcc:13 AS builder

    WORKDIR /app
    
    # Install CMake
    RUN apt-get update && apt-get install -y cmake
    
    # Copy source
    COPY . .
    
    # Build
    RUN mkdir build && cd build \
        && cmake .. \
        && make -j$(nproc)
    
    # ---------- RUNTIME STAGE ----------
    FROM ubuntu:24.04
    
    WORKDIR /app
    
    # Copy compiled binary only
    COPY --from=builder /app/build/vector_engine /app/vector_engine
    
    # Expose HTTP port
    EXPOSE 9090
    
    # Run binary
    CMD ["./vector_engine"]
    