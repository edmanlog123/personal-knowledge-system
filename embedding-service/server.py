from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer

app = Flask(__name__)
model = SentenceTransformer("sentence-transformers/all-MiniLM-L6-v2")

@app.route("/embed", methods=["POST"])
def embed():
    data = request.get_json()
    text = data.get("text", "")

    if not text:
        return jsonify({"error": "text required"}), 400

    embedding = model.encode(text).tolist()
    return jsonify({"vector": embedding})

if __name__ == "__main__":
    app.run(port=8081)
