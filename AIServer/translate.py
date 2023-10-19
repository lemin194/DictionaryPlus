import requests
import json

def translate(text, fromLang, toLang):
	url = "https://translate.googleapis.com/translate_a/single"
	params = {
		"client": "gtx",
		"sl": fromLang,
		"tl": toLang,
		"dt": "t",
		"text": text,
		"op": "translate",
	}
	respond = requests.get(url=url, params=params)
	if (respond.status_code > 299): return text
	jsonres = json.loads(respond.text)
	if (jsonres[0] == None): return text
	ret = ""
	for s in jsonres[0]:
		ret += s[0]
	return ret
	ret = ""
	for s in jsonres[0]:
		ret += s[0]
	return ret

