import cv2
from image_services import load_from_bytes, load_from_file, translate_image
import json

img = load_from_file("temp/Screenshot.png")
# img, content= translate_image(img)
# # print(content)
# cv2.imshow('img', img)
# cv2.waitKey(0)
# cv2.destroyAllWindows()

import json
import requests

headers = {"Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiZjhmYmZiMmYtMDg1YS00OGYxLTk0NjItMWQ3YjBjNzU1MTk4IiwidHlwZSI6ImFwaV90b2tlbiJ9.-naVVZZnkSqRJ6_GCqsCz9qp1S1XllTLE9zmy9kbp0M"}

url="https://api.edenai.run/v2/ocr/ocr"
data={
	"show_original_response": False,
	"fallback_providers": "",
	"providers": "google",
	"language":"en"
}
files = {"file": open("temp/Screenshot.png",'rb')}

response = requests.post(url, data=data, files=files, headers=headers)

result = json.loads(response.text)
print(result)
print(result["google"]["text"])
print(result['google']['bounding_boxes'])

bbs_json = result['google']['bounding_boxes']
bbs = []
for bb_json in bbs_json:
	bbs += [(bb_json['left'], bb_json['top'], bb_json['width'], bb_json['height'])]

print(bbs) 