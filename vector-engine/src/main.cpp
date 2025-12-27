#include <iostream>
#include <string>
#include <vector>

#include "vector_store.h"

// HTTP + JSON
#include "httplib.h"
#include "nlohmann/json.hpp"

using json = nlohmann::json;

static VectorStore store;

// Helpers: basic validation
static bool same_dim(const std::vector<float>& a, const std::vector<float>& b) {
    return a.size() == b.size();
}

int main() {
    // Seed data (temporary). Later Kotlin will POST vectors to /upsert.
    store.add("note-1", {1.0f, 0.0f, 0.0f});
    store.add("note-2", {0.0f, 1.0f, 0.0f});
    store.add("note-3", {0.9f, 0.1f, 0.0f});

    httplib::Server server;

    // Health check
    server.Get("/health", [](const httplib::Request&, httplib::Response& res) {
        res.set_content(R"({"status":"ok"})", "application/json");
    });

    // Search endpoint
    // POST /search
    // { "query_vector": [..], "top_k": 5 }
    server.Post("/search", [](const httplib::Request& req, httplib::Response& res) {
        try {
            auto body = json::parse(req.body);

            if (!body.contains("query_vector") || !body["query_vector"].is_array()) {
                res.status = 400;
                res.set_content(R"({"error":"Missing query_vector array"})", "application/json");
                return;
            }

            int top_k = 5;
            if (body.contains("top_k")) top_k = body["top_k"].get<int>();
            if (top_k <= 0) top_k = 5;

            std::vector<float> query = body["query_vector"].get<std::vector<float>>();

            // Basic guard: empty vector
            if (query.empty()) {
                res.status = 400;
                res.set_content(R"({"error":"query_vector must be non-empty"})", "application/json");
                return;
            }

            // (Optional) dim check against first entry
            // If you haven't inserted anything, return empty results.
            auto results = store.search(query, top_k);

            json out;
            out["results"] = json::array();
            for (auto& [id, score] : results) {
                out["results"].push_back({{"id", id}, {"score", score}});
            }

            res.set_content(out.dump(), "application/json");
        } catch (const std::exception& e) {
            res.status = 400;
            json err = {{"error", std::string("Invalid JSON: ") + e.what()}};
            res.set_content(err.dump(), "application/json");
        }
    });

    std::cout << "Vector service listening on http://0.0.0.0:9090\n";
    // Upsert endpoint
// POST /upsert
// { "id": "uuid", "vector": [..] }
server.Post("/upsert", [](const httplib::Request& req, httplib::Response& res) {
    try {
        auto body = json::parse(req.body);

        if (!body.contains("id") || !body.contains("vector")) {
            res.status = 400;
            res.set_content(R"({"error":"Missing id or vector"})", "application/json");
            return;
        }

        std::string id = body["id"].get<std::string>();
        std::vector<float> vec = body["vector"].get<std::vector<float>>();

        if (id.empty() || vec.empty()) {
            res.status = 400;
            res.set_content(R"({"error":"id and vector must be non-empty"})", "application/json");
            return;
        }

        // Simple strategy for now: append.
        // (We’ll dedupe later if needed.)
        store.add(id, vec);

        res.set_content(R"({"status":"ok"})", "application/json");
    } catch (const std::exception& e) {
        res.status = 400;
        json err = {{"error", std::string("Invalid JSON: ") + e.what()}};
        res.set_content(err.dump(), "application/json");
    }
});

    server.listen("0.0.0.0", 9090);
    return 0;
}
