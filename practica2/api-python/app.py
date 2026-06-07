from flask import Flask, jsonify
import requests
import sqlite3
import os

app = Flask(__name__)

@app.route("/")
def home():
    return "API Flask funcionando"

@app.route("/api/saludo")
def saludo():
    return jsonify({"mensaje": "Hola desde Flask"})

@app.route("/api/pokemon/<nombre>")
def get_pokemon(nombre):
    try:
        url = f"https://pokeapi.co/api/v2/pokemon/{nombre.lower()}"
        response = requests.get(url, timeout=5)
        response.raise_for_status()
        d = response.json()
        return jsonify({
            "nombre": d["name"],
            "id": d["id"],
            "altura": d["height"],
            "peso": d["weight"],
            "experiencia_base": d["base_experience"],
            "tipos": [t["type"]["name"] for t in d["types"]],
            "habilidades": [
                {"nombre": a["ability"]["name"], "oculta": a["is_hidden"]}
                for a in d["abilities"]
            ],
            "stats": {
                s["stat"]["name"]: s["base_stat"] for s in d["stats"]
            },
            "objetos": [h["item"]["name"] for h in d["held_items"]],
            "movimientos": sorted(list(set(
                m["move"]["name"] for m in d["moves"]
            )))[:20],
            "imagen": d["sprites"]["front_default"],
            "imagen_shiny": d["sprites"]["front_shiny"],
            "imagen_oficial": d["sprites"]["other"]["official-artwork"]["front_default"],
            "imagen_gif": d["sprites"]["other"]["showdown"]["front_default"]
            if d["sprites"]["other"].get("showdown") else None,
        })
    except requests.exceptions.HTTPError:
        return jsonify({
            "error": "requests.exceptions.HTTPError",
            "detalle": f"No se encontró el pokemon '{nombre}'"
        }), 500
    except requests.exceptions.ConnectionError:
        return jsonify({
            "error": "requests.exceptions.ConnectionError",
            "detalle": "No se pudo conectar a la PokeAPI"
        }), 500
    except requests.exceptions.Timeout:
        return jsonify({
            "error": "requests.exceptions.Timeout",
            "detalle": "La PokeAPI tardó demasiado en responder"
        }), 500


# --- EXCEPCIÓN 1: Apertura y lectura de archivo ---
@app.route("/api/exception/archivo")
def exception_archivo():
    try:
        with open("archivo_que_no_existe.txt", "r") as f:
            contenido = f.read()
        return jsonify({"mensaje": contenido})
    except FileNotFoundError:
        return jsonify({
            "error": "FileNotFoundError",
            "detalle": "No se pudo abrir el archivo solicitado"
        }), 500
    except PermissionError:
        return jsonify({
            "error": "PermissionError",
            "detalle": "No hay permisos para acceder al archivo"
        }), 500


# --- EXCEPCIÓN 2: Acceso a base de datos ---
@app.route("/api/exception/bbdd")
def exception_bbdd():
    try:
        conn = sqlite3.connect("base_de_datos_inexistente/bd.db")
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM tabla_inexistente")
        rows = cursor.fetchall()
        return jsonify({"datos": rows})
    except sqlite3.OperationalError:
        return jsonify({
            "error": "OperationalError",
            "detalle": "Error operando con la base de datos"
        }), 500
    except Exception:
        return jsonify({
            "error": "ErrorBBDD",
            "detalle": "Error interno de base de datos"
        }), 500


# --- EXCEPCIÓN 3: Llamada a API de terceros (Pokémon) ---
@app.route("/api/exception/pokemon")
def exception_pokemon():
    try:
        url = "https://pokeapi.co/api/v2/pokemon/pokemon-que-no-existe-99999"
        response = requests.get(url, timeout=5)
        response.raise_for_status()
        return jsonify(response.json())
    except requests.exceptions.HTTPError:
        return jsonify({
            "error": "requests.exceptions.HTTPError",
            "detalle": "La PokeAPI devolvió un error"
        }), 500
    except requests.exceptions.ConnectionError as e:
        return jsonify({
            "error": "requests.exceptions.ConnectionError",
            "detalle": "No se pudo conectar a la PokeAPI"
        }), 500
    except requests.exceptions.Timeout as e:
        return jsonify({
            "error": "requests.exceptions.Timeout",
            "detalle": "La PokeAPI tardó demasiado en responder"
        }), 500


if __name__ == "__main__":
    app.run(debug=True, port=5000)