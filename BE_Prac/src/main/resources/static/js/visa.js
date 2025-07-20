document.addEventListener('DOMContentLoaded', () => {
    const visaForm = document.getElementById('visa-form');
    const getVisaForm = document.getElementById('get-visa-form');
    const visaList = document.getElementById('visa-list');

    // 비자 목록 불러오기
    const fetchVisas = async (passportNo) => {
        const response = await fetch(`/api/v1/visa/${passportNo}`);
        if (response.ok) {
            const visas = await response.json();
            visaList.innerHTML = '';
            visas.forEach(visa => {
                const visaItem = document.createElement('div');
                visaItem.className = 'visa-item';
                visaItem.innerHTML = `
                    <p><strong>여권 번호:</strong> ${visa.passportNo}</p>
                    <p><strong>국가 코드:</strong> ${visa.countryCode}</p>
                    <p><strong>발급일:</strong> ${visa.startDate}</p>
                    <p><strong>만료일:</strong> ${visa.endDate}</p>
                    <button onclick="deleteVisa('${visa.passportNo}', '${visa.countryCode}')">삭제</button>
                `;
                visaList.appendChild(visaItem);
            });
        } else {
            visaList.innerHTML = '<p>해당 여권 번호의 비자를 찾을 수 없습니다.</p>';
        }
    };

    // 비자 생성
    visaForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const visaData = {
            passportNo: document.getElementById('passportNo').value,
            countryCode: document.getElementById('countryCode').value
        };

        const response = await fetch('/api/v1/visa', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(visaData)
        });

        if (response.ok) {
            alert('비자가 발급되었습니다.');
            visaForm.reset();
            // 비자 발급 후, 해당 여권의 비자 목록을 다시 불러올 수 있습니다.
            const passportNoForVisa = document.getElementById('get-passportNo').value;
            if(passportNoForVisa) {
                fetchVisas(passportNoForVisa);
            }
        } else {
            alert('비자 발급에 실패했습니다. 여권 번호와 국가 코드를 확인해주세요.');
        }
    });

    // 비자 조회
    getVisaForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const passportNo = document.getElementById('get-passportNo').value;
        fetchVisas(passportNo);
    });

    // 비자 삭제
    window.deleteVisa = async (passportNo, countryCode) => {
        const response = await fetch(`/api/v1/visa/${passportNo}/${countryCode}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            const currentPassportNo = document.getElementById('get-passportNo').value;
            if(currentPassportNo) {
                fetchVisas(currentPassportNo);
            }
        } else {
            alert('비자 삭제에 실패했습니다.');
        }
    };
});
