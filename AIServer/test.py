import cv2
from image_services import load_from_bytes
import json

img = load_from_bytes(json.load(open('test.json', 'r'))['file'])

cv2.imshow('img', img)
cv2.waitKey(0)
cv2.destroyAllWindows()