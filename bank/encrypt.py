import os
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad
import base64

private_key = "SHINHAN_HACKATHON_LSH_LJJ_HDW_GL"

# 키 길이 확인
if len(private_key) not in [16, 24, 32]:
    raise ValueError("Incorrect AES key length. It should be 16, 24, or 32 bytes.")

def encrypt(text, key):
    cipher = AES.new(key.encode('utf-8'), AES.MODE_ECB)
    encrypted = cipher.encrypt(pad(text.encode('utf-8'), AES.block_size))
    return base64.b64encode(encrypted).decode('utf-8')

# api-key.txt를 읽어서 암호화한 후 api-key-encrypted.txt에 저장
with open('api-key.txt', 'r') as f:
    lines = f.readlines()

encrypted_lines = []
for line in lines:
#     key_name, key_value = line.strip().split(':')
    encrypted_key = encrypt(line, private_key)
    encrypted_lines.append(f"{encrypted_key}\n")

with open('src/main/resources/api-key-encrypted.txt', 'w') as f:
    f.writelines(encrypted_lines)

print("API keys have been encrypted and saved to api-key-encrypted.txt")