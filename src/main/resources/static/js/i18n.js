let currentLang = 'en';

async function loadLanguage(lang) {
    try {
        const response = await fetch(`/i18n/${lang}.json`);
        const translations = await response.json();

        // Apply translations using data-i18n attributes
        document.querySelectorAll('[data-i18n]').forEach(elem => {
            const key = elem.getAttribute('data-i18n');
            if (translations[key]) {
                elem.innerText = translations[key];
            }
        });

        // Set document title if available
        if (translations['app.title']) {
            document.title = translations['app.title'];
        }

    } catch (err) {
        console.error("Error loading language file:", err);
    }
}

function changeLanguage(lang) {
    currentLang = lang;
    localStorage.setItem('lang', lang);
    loadLanguage(lang);

    // Update toggle UI
    const icon = document.getElementById('toggleIcon');
    const langText = document.getElementById('languageText');

    if (lang === 'mh') {
        icon.classList.remove('fa-toggle-off');
        icon.classList.add('fa-toggle-on');
    } else {
        icon.classList.remove('fa-toggle-on');
        icon.classList.add('fa-toggle-off');
    }
}

function toggleLanguage() {
    const current = localStorage.getItem('lang') || 'en';
    const newLang = current === 'en' ? 'mh' : 'en';
    changeLanguage(newLang);
}

document.addEventListener('DOMContentLoaded', () => {
    const savedLang = localStorage.getItem('lang') || 'en';
    changeLanguage(savedLang);
});
