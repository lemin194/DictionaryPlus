import time

from flask import Flask, request, jsonify
from flask_cors import CORS
from correcttext import correct as correcttext
import requests

app = Flask(__name__)

app.config['CORS_HEADERS'] = 'Content-Type'

@app.route('/', methods=['GET', 'POST'])
async def index():
    return "Hello world"


@app.route('/autocorrect/', methods=['POST'])
async def route_autocorrect():
  data = request.get_json()
  text = data.get("text", "")
  lang = data.get("sl", "en")
  if (lang not in ["en", "vi"]):
     return f"Language {lang} is not supported, supported languages are 'en' or 'vi'", 400
  corrected = correcttext(text, lang)
  return jsonify({
     "content": corrected,
  })

from PIL import Image
import io
import base64
import numpy as np

import cv2
from image_services import (
   translate_image,
   load_from_bytes, load_from_file, save_to_bytes)

from speech_services import (
   speech_analysis,
   load_from_bytes as load_speech_from_bytes
)

@app.route("/translateimage/", methods=['POST'])
async def route_translateimage():
   data = request.get_json()
   file = data.get("file", "")
   if (len(file) == 0): return "No image sent.", 400
   # print(bytes.decode(file))

   image = load_from_bytes(file)
   print(image.shape)
   ret_image, ret_content = translate_image(image)
   return jsonify({
      "file": save_to_bytes(ret_image),
      "content": ret_content,
   })

@app.route("/speechanalysis/", methods=['POST'])
async def route_speechanalysis():
   print('received request')
   data = request.get_json()
   print('DATA:',data)
   file = data.get("file", "")
   src = data.get("src", "")
   if (len(file) == 0): return "No file sent.", 400
   load_speech_from_bytes(file)
   return jsonify({
      'content': speech_analysis(src)
   })


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=9876, debug=True)