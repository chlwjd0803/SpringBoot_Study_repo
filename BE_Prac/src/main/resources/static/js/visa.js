document.addEventListener('DOMContentLoaded', () => {
    const visaForm = document.getElementById('visa-form');
    const getVisaForm = document.getElementById('get-visa-form');
    const visaList = document.getElementById('visa-list');
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

    // 비자 목록 불러오기
    const fetchVisas = async (passportNo) => {
        try {
            const response = await fetch(`/api/v1/visa/${passportNo}`);
            if (response.ok) {
                const visas = await response.json();
                visaList.innerHTML = '';
                if (visas.length === 0) {
                    visaList.innerHTML = '<p>해당 여권에 발급된 비자가 없습니다.</p>';
                    return;
                }
                visas.forEach(visa => {
                    const visaItem = document.createElement('div');
                    visaItem.className = 'visa-item';
                    visaItem.innerHTML = `
                        <p><strong>여권 번호:</strong> ${visa.passportNo}</p>
                        <p><strong>방문 국가:</strong> ${visa.countryCode}</p>
                        <p><strong>발급일:</strong> ${visa.startDate}</p>
                        <p><strong>만료일:</strong> ${visa.endDate}</p>
                    `;
                    const deleteButton = document.createElement('button');
                    deleteButton.textContent = '삭제';
                    deleteButton.onclick = () => deleteVisa(visa.passportNo, visa.countryCode);
                    visaItem.appendChild(deleteButton);
                    visaList.appendChild(visaItem);
                });
            } else {
                visaList.innerHTML = '<p>해당 여권 번호의 비자를 찾을 수 없습니다.</p>';
            }
        } catch (error) {
            console.error('Error fetching visas:', error);
            visaList.innerHTML = '<p>비자 정보를 불러오는 데 실패했습니다.</p>';
        }
    };

    // 비자 생성
    visaForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const visaData = {
            passportNo: document.getElementById('passportNo').value,
            countryCode: document.getElementById('countryCode').value
        };

        if (!visaData.countryCode) {
            alert('방문 국가를 선택해주세요.');
            return;
        }

        try {
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
                const passportNoForVisa = document.getElementById('get-passportNo').value;
                if (passportNoForVisa) {
                    fetchVisas(passportNoForVisa);
                }
            } else {
                const errorData = await response.json();
                alert(`비자 발급 실패: ${errorData.message || '여권 번호와 국가 코드를 확인해주세요.'}`);
            }
        } catch (error) {
            console.error('Error creating visa:', error);
            alert('비자 발급 중 오류가 발생했습니다.');
        }
    });

    // 비자 조회
    getVisaForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const passportNo = document.getElementById('get-passportNo').value;
        if (passportNo) {
            fetchVisas(passportNo);
        }
    });

    // 비자 삭제
    window.deleteVisa = async (passportNo, countryCode) => {
        if (!confirm(`정말로 해당 비자(여권:${passportNo}, 국가:${countryCode})를 삭제하시겠습니까?`)) return;

        try {
            const response = await fetch(`/api/v1/visa/${passportNo}/${countryCode}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                const currentPassportNo = document.getElementById('get-passportNo').value;
                if (currentPassportNo) {
                    fetchVisas(currentPassportNo);
                }
            } else {
                alert('비자 삭제에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error deleting visa:', error);
            alert('비자 삭제 중 오류가 발생했습니다.');
        }
    };

    fetchCountries();
});