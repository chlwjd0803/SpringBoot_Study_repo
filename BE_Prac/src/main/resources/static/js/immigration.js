document.addEventListener('DOMContentLoaded', () => {
    const verifySection = document.getElementById('verify-section');
    const chatSection = document.getElementById('chat-section');
    const verifyForm = document.getElementById('verify-form');
    const chatForm = document.getElementById('chat-form');
    const chatBox = document.getElementById('chat-box');
    const chatInput = document.getElementById('chat-input');
    const typingIndicator = document.getElementById('typing-indicator');

    // 여권 인증
    verifyForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const passportNo = document.getElementById('passportNo').value;

        if (!passportNo) {
            alert('여권 번호를 입력해주세요.');
            return;
        }

        try {
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
                addMessage('bot', '여권 인증이 완료되었습니다. 잠시 후 입국 심사를 시작하겠습니다.');
                // 초기 메시지 로딩 등 추가적인 상호작용이 필요하다면 여기에 구현
            } else {
                const errorData = await response.json();
                alert(`여권 인증 실패: ${errorData.message || '알 수 없는 오류'}`);
            }
        } catch (error) {
            console.error('Error during verification:', error);
            alert('인증 과정에서 오류가 발생했습니다.');
        }
    });

    // 채팅 메시지 전송
    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const message = chatInput.value.trim();
        if (!message) return;

        addMessage('user', message);
        chatInput.value = '';
        showTypingIndicator(true);

        try {
            const response = await fetch('/api/v1/immigration/chat', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message })
            });

            const data = await response.json();
            showTypingIndicator(false);

            if (data.action) { // 심사 종료
                let resultMessage = `심사 결과: ${data.action === 'approve' ? '승인' : '거절'}`;
                if (data.reason) {
                    resultMessage += `\n사유: ${data.reason}`;
                }
                addMessage('bot', resultMessage);
                chatForm.style.display = 'none';
            } else { // 심사 계속
                const assistantMessage = data.choices[0].message.content;
                addMessage('bot', assistantMessage);
            }
        } catch (error) {
            showTypingIndicator(false);
            console.error('Error during chat:', error);
            addMessage('bot', '오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
        }
    });

    function addMessage(sender, text) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('chat-message', `${sender}-message`);
        messageElement.textContent = text;
        chatBox.appendChild(messageElement);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    function showTypingIndicator(show) {
        typingIndicator.style.display = show ? 'flex' : 'none';
        if (show) {
            chatBox.scrollTop = chatBox.scrollHeight;
        }
    }
});