import cv2
from image_services import load_from_bytes, load_from_file, translate_image
import json

img = load_from_file("temp/Screenshot.png")
img, content= translate_image(img)
print(content)
cv2.imshow('img', img)
cv2.waitKey(0)
cv2.destroyAllWindows()