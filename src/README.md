# 📦 CustomChest - 고급 창고 시스템 플러그인

> **Minecraft 1.20.1 / Paper 기반**  
> 안전하고, 확장성 있고, 유저 친화적인 개인 창고 플러그인

---

## 🧩 주요 기능

- ✅ **개인 전용 창고** 최대 5개까지 확장 가능
- 🔐 **1인 전용 락 시스템** – 동시 열람 방지, 자동 해제 타이머 포함
- 💾 **비동기 저장 + 캐싱 시스템** – 렉 없이 안전한 저장
- 🔒 **권한 기반 창고 제한** – `customchest.chest.1` 등으로 제한 설정 가능
- 🧰 **자동 저장 & 자동 삭제 기능** – 설정된 간격에 따라 자동 관리
- 👥 **다른 유저 창고 열람** 기능 (퍼미션 기반)
- 🧪 **GUI 기반 접근** – `/창고` 명령어로 간편하게 열람

---

## 📸 미리 보기

| 기본 GUI | 다른 유저 창고 열람 | 사용 중 락 처리 |
|----------|----------------------|------------------|
| ![GUI](https://via.placeholder.com/300x160.png?text=Main+Storage+GUI) | ![OpenOther](https://via.placeholder.com/300x160.png?text=Open+Others) | ![Locked](https://via.placeholder.com/300x160.png?text=Storage+Locked) |

---

## 🔧 명령어

| 명령어 | 설명 |
|--------|------|
| `/창고` | 메인 GUI 열기 |
| `/창고 열기 <닉네임> <번호>` | 다른 유저의 창고 열기 |
| `/창고 리로드` | 설정 리로드 (config, gui, messages 등) |

---

## ⚙️ 퍼미션

| 퍼미션 노드 | 설명 |
|--------------|------|
| `customchest.open` | 자신의 창고 열기 |
| `customchest.open.other` | 다른 유저의 창고 열람 |
| `customchest.chest.1` ~ `5` | N번 창고 열 수 있는 권한 |

---

## 🛠 설치 방법

1. `CustomChest-x.x.x.jar` 다운로드
2. 서버 `/plugins` 폴더에 넣기
3. 서버 재시작 or 리로드
4. `/창고` 명령어로 바로 시작!

---

## 🧙 플러그인 구조

```plaintext
CustomChest/
├── config.yml               # 설정 파일
├── gui.yml                  # GUI 아이템 설정
├── messages.yml             # 메시지 파일
├── data/
│   └── <UUID>_1.yml         # 창고 데이터 (비동기 저장)
└── autosave/
    └── 백업파일_타임스탬프.yml
```

---

## 💬 라이선스

이 플러그인은 MIT 라이선스 하에 배포됩니다.  
자유롭게 사용, 수정, 재배포가 가능하며 상업적 사용도 허용됩니다.  
단, 원 저작자 명시 및 라이선스 사본 포함은 필수입니다.

[MIT License 보기](LICENSE)
