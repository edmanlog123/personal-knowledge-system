#pragma once

#include <string>
#include <vector>
#include <utility>

struct VectorEntry {
    std::string id;
    std::vector<float> vec;
};

class VectorStore {
public:
    void add(const std::string& id, const std::vector<float>& vec);

    std::vector<std::pair<std::string, float>>
    search(const std::vector<float>& query, int k) const;

private:
    std::vector<VectorEntry> index;

    static float cosine_similarity(
        const std::vector<float>& a,
        const std::vector<float>& b
    );
};
