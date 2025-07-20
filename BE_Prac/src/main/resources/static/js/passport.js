document.addEventListener('DOMContentLoaded', () => {
    const passportForm = document.getElementById('passport-form');
    const passportList = document.getElementById('passport-list');

    // 여권 목록 불러오기
    const fetchPassports = async () => {
        const response = await fetch('/api/v1/passport');
        const passports = await response.json();
        passportList.innerHTML = '';
        passports.forEach(passport => {
            const passportItem = document.createElement('div');
            passportItem.className = 'passport-item';
            passportItem.innerHTML = `
                <p><strong>여권 번호:</strong> ${passport.passportNo}</p>
                <p><strong>이름:</strong> ${passport.fullName}</p>
                <p><strong>국적:</strong> ${passport.countryName}</p>
                <p><strong>생년월일:</strong> ${passport.birthDate}</p>
                <p><strong>발급일:</strong> ${passport.issueDate}</p>
                <p><strong>만료일:</strong> ${passport.expiryDate}</p>
                <button onclick="deletePassport('${passport.passportNo}')">삭제</button>
            `;
            passportList.appendChild(passportItem);
        });
    };

    // 여권 생성
    passportForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const passportData = {
            passportNo: document.getElementById('passportNo').value,
            fullName: document.getElementById('fullName').value,
            countryCode: document.getElementById('countryCode').value,
            birthDate: document.getElementById('birthDate').value
        };

        const response = await fetch('/api/v1/passport', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(passportData)
        });

        if (response.ok) {
            fetchPassports();
            passportForm.reset();
        } else {
            alert('여권 발급에 실패했습니다. 입력 형식을 확인해주세요.');
        }
    });

    // 여권 삭제
    window.deletePassport = async (passportNo) => {
        const response = await fetch(`/api/v1/passport/${passportNo}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            fetchPassports();
        } else {
            alert('여권 삭제에 실패했습니다.');
        }
    };

    fetchPassports();
});
