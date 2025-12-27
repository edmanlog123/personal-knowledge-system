cmake_minimum_required(VERSION 3.15)
project(vector_engine)

set(CMAKE_CXX_STANDARD 17)

add_executable(vector_engine
    src/main.cpp
    src/vector_store.cpp
)
