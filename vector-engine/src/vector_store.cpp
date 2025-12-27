#include "vector_store.h"
#include <cmath>
#include <algorithm>

void VectorStore::add(const std::string& id, const std::vector<float>& vec) {
    index.push_back({id, vec});
}

float VectorStore::cosine_similarity(
    const std::vector<float>& a,
    const std::vector<float>& b
) {
    float dot = 0.0f;
    float norm_a = 0.0f;
    float norm_b = 0.0f;

    for (size_t i = 0; i < a.size(); i++) {
        dot += a[i] * b[i];
        norm_a += a[i] * a[i];
        norm_b += b[i] * b[i];
    }

    return dot / (std::sqrt(norm_a) * std::sqrt(norm_b));
}

std::vector<std::pair<std::string, float>>
VectorStore::search(const std::vector<float>& query, int k) const {

    std::vector<std::pair<std::string, float>> results;

    for (const auto& entry : index) {
        float score = cosine_similarity(query, entry.vec);
        results.push_back({entry.id, score});
    }

    std::partial_sort(
        results.begin(),
        results.begin() + std::min(k, (int)results.size()),
        results.end(),
        [](const auto& a, const auto& b) {
            return a.second > b.second;
        }
    );

    if ((int)results.size() > k) {
        results.resize(k);
    }

    return results;
}
