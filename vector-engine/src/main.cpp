#include <iostream>
#include "vector_store.h"

int main() {
    VectorStore store;

    store.add("note-1", {1.0f, 0.0f, 0.0f});
    store.add("note-2", {0.0f, 1.0f, 0.0f});
    store.add("note-3", {0.9f, 0.1f, 0.0f});

    std::vector<float> query = {1.0f, 0.0f, 0.0f};

    auto results = store.search(query, 2);

    for (const auto& [id, score] : results) {
        std::cout << id << " -> " << score << std::endl;
    }

    return 0;
}
