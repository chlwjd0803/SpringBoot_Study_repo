document.addEventListener('DOMContentLoaded', () => {
    const passportForm = document.getElementById('passport-form');
    const passportList = document.getElementById('passport-list');
    const countrySelect = document.getElementById('countryCode');

    // 국가 목록 불러오기
    const fetchCountries = async () => {
        try {
            const response = await fetch('/api/v1/countries');
            if (!response.ok) throw new Error('Failed to fetch countries');
            const countries = await response.json();
            countries.forEach(country => {
                const option = document.createElement('option');
                option.value = country.countryCode;
                option.textContent = `${country.countryName} (${country.countryCode})`;
                countrySelect.appendChild(option);
            });
        } catch (error) {
            console.error('Error fetching countries:', error);
            const option = document.createElement('option');
            option.textContent = '국가 목록을 불러올 수 없습니다.';
            option.disabled = true;
            countrySelect.appendChild(option);
        }
    };

    // 여권 목록 불러오기
    const fetchPassports = async () => {
        try {
            const response = await fetch('/api/v1/passport');
            if (!response.ok) throw new Error('Failed to fetch passports');
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
                `;
                const deleteButton = document.createElement('button');
                deleteButton.textContent = '삭제';
                deleteButton.onclick = () => deletePassport(passport.passportNo);
                passportItem.appendChild(deleteButton);
                passportList.appendChild(passportItem);
            });
        } catch (error) {
            console.error('Error fetching passports:', error);
            passportList.innerHTML = '<p>여권 목록을 불러오는 데 실패했습니다.</p>';
        }
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

        if (!passportData.countryCode) {
            alert('국적을 선택해주세요.');
            return;
        }

        try {
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
                const errorData = await response.json();
                alert(`여권 발급 실패: ${errorData.message || '입력 형식을 확인해주세요.'}`);
            }
        } catch (error) {
            console.error('Error creating passport:', error);
            alert('여권 발급 중 오류가 발생했습니다.');
        }
    });

    // 여권 삭제
    window.deletePassport = async (passportNo) => {
        if (!confirm(`정말로 여권(${passportNo})을 삭제하시겠습니까?`)) return;

        try {
            const response = await fetch(`/api/v1/passport/${passportNo}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                fetchPassports();
            } else {
                alert('여권 삭제에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error deleting passport:', error);
            alert('여권 삭제 중 오류가 발생했습니다.');
        }
    };

    fetchCountries();
    fetchPassports();
});