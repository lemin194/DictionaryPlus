import cv2


import cv2
import pytesseract

import json
from translate import translate
from correcttext import correct
import re

import numpy as np
from PIL import ImageFont, ImageDraw, Image
import cv2
import time
import math

## Use simsum.ttc to write Chinese.
fontpath = "./resources/fonts/font.ttf" # <== 这里是宋体路径
font = ImageFont.truetype(fontpath, 32)
print(font.getbbox("m"))
print(font.getbbox("a"))

def draw_lines(img, bbox, lines, fontpath, fill):
    if (max(len(line) for line in lines) == 0
        or len(lines) == 0): return img
    print(lines)
    x, y, w, h = bbox
    font = ImageFont.truetype(fontpath, 64)
    fontsize = 64
    linew, lineh = w, h / len(lines)
    for line in lines:
        _, __, w0, h0 = font.getbbox(line)
        fontsize = int(math.ceil(min(fontsize, 64 * linew / w0, 64 * lineh / h0)))
    font = ImageFont.truetype(fontpath, fontsize)
    for i, line in enumerate(lines):
        pos = (x, y + i * lineh)
        img_pil = Image.fromarray(img)
        draw = ImageDraw.Draw(img_pil)
        draw.text(pos, line, font=font, fill=fill)
        img = np.array(img_pil)

    return img


def get_lines(text):
    text = str.join('\n', re.split(r'[\s\t]*\n[\s\t]*',text.replace("‘", " ").strip()))
    text = correct(text)
    lines = re.split(r'[\s\t]*\n[\s\t]*',translate(text, "auto", "vi").strip())
    return lines

import io
import base64

def load_from_file(filepath):
    return cv2.imread(filepath)
def load_from_bytes(input):
    return cv2.imdecode(
        np.frombuffer(base64.b64decode(input), np.int8), cv2.IMREAD_COLOR)
def save_to_bytes(image):
    _, encoded_image = cv2.imencode('.png', image)
    image_bytes = np.array(encoded_image).tobytes()
    return base64.b64encode(image_bytes).decode()
 
def translate_image(img):
    print(type(img), img.shape)
    print(img.max(), img.mean(), img.std())
    # img = cv2.imread("temp/words.png")
    # print(type(img), img.shape)
    # print(img.max(), img.mean(), img.std())
    
    # Preprocessing the image starts
    
    # Convert the image to gray scale
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    
    # Performing OTSU threshold
    ret, thresh1 = cv2.threshold(gray, 0, 255, cv2.THRESH_OTSU | cv2.THRESH_BINARY_INV)
    
    # Specify structure shape and kernel size. 
    # Kernel size increases or decreases the area 
    # of the rectangle to be detected.
    # A smaller value like (10, 10) will detect 
    # each word instead of a sentence.
    rect_kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (18, 18))

    # Applying dilation on the threshold image
    dilation = cv2.dilate(thresh1, rect_kernel, iterations = 1)
    
    # Finding contours
    contours, hierarchy = cv2.findContours(dilation, cv2.RETR_EXTERNAL, 
                                                    cv2.CHAIN_APPROX_NONE)
    
    ret_img, ret_content = img.copy(), ""

    for cnt in contours:
        x, y, w, h = cv2.boundingRect(cnt)
        
        cropped = ret_img[y:y + h, x:x + w]
        
        
        text = pytesseract.image_to_string(cropped)
        lines = get_lines(text)

        cropped[:] = [128, 240, 240]
        ret_img = draw_lines(ret_img, (x, y, w, h), lines, fontpath, (16, 16, 16))
        # Appending the text into file
        
        ret_content += str.join('\n', lines)
        ret_content += "\n"
    
    return ret_img, ret_content

