document.addEventListener('DOMContentLoaded', () => {
    const verifySection = document.getElementById('verify-section');
    const chatSection = document.getElementById('chat-section');
    const verifyForm = document.getElementById('verify-form');
    const chatForm = document.getElementById('chat-form');
    const chatBox = document.getElementById('chat-box');
    const chatInput = document.getElementById('chat-input');

    // 여권 인증
    verifyForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const passportNo = document.getElementById('passportNo').value;

        const response = await fetch('/api/v1/passport/verify', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ passportNo })
        });

        if (response.ok) {
            verifySection.style.display = 'none';
            chatSection.style.display = 'block';
            addMessage('system', '여권 인증이 완료되었습니다. 입국 심사를 시작합니다.');
        } else {
            alert('여권 인증에 실패했습니다.');
        }
    });

    // 채팅 메시지 전송
    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const message = chatInput.value;
        if (!message) return;

        addMessage('user', message);
        chatInput.value = '';

        const response = await fetch('/api/v1/immigration/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ message })
        });

        const data = await response.json();

        if (data.action) { // 심사 종료
            let resultMessage = `심사 결과: ${data.action === 'approve' ? '승인' : '반려'}`;
            if(data.reason) {
                resultMessage += `, 사유: ${data.reason}`;
            }
            addMessage('system', resultMessage);
            chatForm.style.display = 'none';
        } else { // 심사 계속
            const assistantMessage = data.choices[0].message.content;
            addMessage('assistant', assistantMessage);
        }
    });

    function addMessage(sender, text) {
        const messageElement = document.createElement('div');
        messageElement.innerHTML = `<strong>[${sender}]</strong> ${text}`;
        chatBox.appendChild(messageElement);
        chatBox.scrollTop = chatBox.scrollHeight;
    }
});
