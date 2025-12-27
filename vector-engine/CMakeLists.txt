cmake_minimum_required(VERSION 3.15)
project(vector_engine)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

include(FetchContent)

# JSON
FetchContent_Declare(
  nlohmann_json
  GIT_REPOSITORY https://github.com/nlohmann/json.git
  GIT_TAG v3.11.3
)
FetchContent_MakeAvailable(nlohmann_json)

# HTTP (single-header lib; build target provided by repo)
FetchContent_Declare(
  cpp_httplib
  GIT_REPOSITORY https://github.com/yhirose/cpp-httplib.git
  GIT_TAG v0.15.3
)
FetchContent_MakeAvailable(cpp_httplib)

add_executable(vector_engine
  src/main.cpp
  src/vector_store.cpp
)

target_link_libraries(vector_engine
  PRIVATE nlohmann_json::nlohmann_json
)

# cpp-httplib is header-only; some versions expose a target, some don't.
# We'll just add include dirs from the fetched source.
target_include_directories(vector_engine PRIVATE ${cpp_httplib_SOURCE_DIR})
