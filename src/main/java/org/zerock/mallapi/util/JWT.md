
ACCESS_TOKEN 과 REFRESH_TOKEN

ACCESS_TOKEN : 타임아웃 짧게 설정 WHY? 탈취 당해도 위험을 줄일 수 있기 때문에
REFRESH_TOKEN : ACCESS_TOKEN이 만료되면 REFRESH_TOKEN을 이용해 새로운 ACCESS_TOKEN을 발급한다.

# * ACCESS_TOKEN 이 없거나 잘못된 JWT => 예외 메세지 발생
# * ACCESS_TOKEN 의 유효기간이 남아있는 경우 => 토큰 그대로 전송
# * ACCESS_TOKEN 만료 / REFRESH_TOKEN 만료 아닌 경우 => 새로운 ACCESS_TOKEN
# * REFRESH_TOKEN 의 얼마 남지 않은 유효 기간 => NEW REFRESH_TOKEN
# * REFRESH_TOKEN 의 유효 기간 충분 => 기존 REFRESH_TOKEN