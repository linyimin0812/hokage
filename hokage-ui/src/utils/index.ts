import { Models } from './model'
import { Terminal } from 'xterm'

export const queryString = () => {
    let _queryString: { [key: string]: any } = {};
    const _query = window.location.search.substr(1);
    const _vars = _query.split('&');
    _vars.forEach((v, i) => {
        const _pair = v.split('=');
        if (!_queryString.hasOwnProperty(_pair[0])) {
            _queryString[_pair[0]] = decodeURIComponent(_pair[1]);
        } else if (typeof _queryString[_pair[0]] === 'string') {
            _queryString[_pair[0]] = [_queryString[_pair[0]], decodeURIComponent(_pair[1])];
        } else {
            _queryString[_pair[0]].push(decodeURIComponent(_pair[1]));
        }
    });
    return _queryString;
}

/**
 * generate a random integer, inclusive max
 * @param max
 */
export const randomInt = (max: number): number => {
    max = Math.ceil(max)
    return Math.floor(Math.random() * (max + 1))
}

/**
 * calculate string hashcode
 * @param text
 */
export const hashCode = (text: string): number => {
    let hash = 5381
    let length = text.length
    while (length) {
        hash = (hash * 33) ^ text.charCodeAt(--length)
    }
    return hash >>> 0;
}



export const randomColor = (text: string): string => {
    const colorList: string[] = Models.get('serverLabelColor')
    return colorList[hashCode(text) % colorList.length]
}

export const setHokageUid = (id: number) => {
    if (!id) id = 0
    window.localStorage.setItem('hokageUid', id + '')
}

export const getHokageUid = (): number => {
    const hokageUid = window.localStorage.getItem('hokageUid')
    return hokageUid ? parseInt(hokageUid) : 0
}

export const removeHokageUid = () => {
    window.localStorage.removeItem('hokageUid')
}

export const xtermSpinner = (terminal: Terminal): NodeJS.Timeout => {
    const spinner = {
        "interval": 100,
            "frames": [
            "🧑⚽️                🧑 ",
            "🧑 ⚽️               🧑 ",
            "🧑  ⚽️              🧑 ",
            "🧑   ⚽️             🧑 ",
            "🧑    ⚽️            🧑 ",
            "🧑     ⚽️           🧑 ",
            "🧑      ⚽️          🧑 ",
            "🧑       ⚽️         🧑 ",
            "🧑        ⚽️        🧑 ",
            "🧑         ⚽️       🧑 ",
            "🧑          ⚽️      🧑 ",
            "🧑           ⚽️     🧑 ",
            "🧑            ⚽️    🧑 ",
            "🧑             ⚽️   🧑 ",
            "🧑              ⚽️  🧑 ",
            "🧑               ⚽️ 🧑 ",
            "🧑                ⚽️🧑 ",
            "🧑               ⚽️ 🧑 ",
            "🧑              ⚽️  🧑 ",
            "🧑             ⚽️   🧑 ",
            "🧑            ⚽️    🧑 ",
            "🧑           ⚽️     🧑 ",
            "🧑          ⚽️      🧑 ",
            "🧑         ⚽️       🧑 ",
            "🧑        ⚽️        🧑 ",
            "🧑       ⚽️         🧑 ",
            "🧑      ⚽️          🧑 ",
            "🧑     ⚽️           🧑 ",
            "🧑    ⚽️            🧑 ",
            "🧑   ⚽️             🧑 ",
            "🧑  ⚽️              🧑 ",
            "🧑 ⚽️               🧑 ",
            "🧑⚽️                🧑 ",
        ]
    }
    let i = 0
    return setInterval(() => {
        const { frames } = spinner;
        // eslint-disable-next-line no-octal-escape
        terminal.write('\x1bc')
        terminal.writeln('connecting ' + frames[i = ++i % frames.length])
    }, spinner.interval)

}
