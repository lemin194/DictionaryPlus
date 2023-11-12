import requests
import string
import json
import numpy as np
from stringcmp import highlight_differences, is_punctuation, is_whitespace
import base64


def load_from_bytes(input):
	bytes_string = base64.b64decode(input)
	with open('temp/record_tmp.mp3', 'wb') as f:
		f.write(bytes_string)

def stt(lang="en", path='temp/record_tmp.mp3'):
	url = "https://transcribe.whisperapi.com"
	headers = {
	'Authorization': 'Bearer PVD42ZJYK89N87FDGNEUCMXN6AY7NTIJ'
	}
	file = {'file': open(path, 'rb')}
	data = {
	"fileType": "mp3", #default is wav
	"diarization": "false",
	"numSpeakers": "1",
	"initialPrompt": "",
	"language": lang,
	"task": "transcribe",
	"callbackURL": "" 
	}
	response = requests.post(url, headers=headers, files=file, data=data)
	return response

def remove_characters(input_string, characters_to_remove):
    result = ""

    for char in input_string:
        if char not in characters_to_remove:
            result += char

    return result

def speech_analysis(source_sentence=""):
	res = stt()
	if res.status_code != 200: return
	res = json.loads(res.text)
	print(res)
	text = res['text']
	confi = []
	spoken_words = []
	rmchars = string.whitespace + string.punctuation
	for seg in res['segments']:
		for wordobj in seg['whole_word_timestamps']:
			word = remove_characters(wordobj['word'], rmchars)
			prob = wordobj['probability']
			prob **= 0.5 
			confi += [(word, prob)]
			# print(word)
			spoken_words += [word]
	# print(confi)
	if (source_sentence.strip() == ""): return

	spoken_sentence = ' '.join(spoken_words)

	print("\n")
	print("Source:", source_sentence)
	print("Spoken:", spoken_sentence)
	diff = highlight_differences(source_sentence, spoken_sentence)

	tokens, missing = diff
	# print('tokens:',tokens)
	# print('missing:', missing)
	# print('confi:', confi)
	confi_i = 0
	final_confi = []
	final_accuracy = []
	for i, token in enumerate(tokens):
		word = token
		prob = 0.0
		if (i in missing):
			prob = 0.0
			final_accuracy += [prob]
		elif is_punctuation(token) or is_whitespace(token):
			prob = 1.0
		else:
			while True:
				word, prob = confi[confi_i]
				if word == token: break
				confi_i += 1
			# print(confi[confi_i][0], token, prob)
			confi_i += 1
			final_accuracy += [prob]
		# print(word, prob, confi[confi_i - 1], )
		final_confi += [[word, prob]]
	final_accuracy = np.array(final_accuracy).mean()
	# print(final_confi)
	print("Accuracy: ", final_accuracy)
	return final_confi


if __name__ == '__main__':
	speech_analysis("Winter holidays are my favorite because I get to build snowmen and have snowball fights with friends.")