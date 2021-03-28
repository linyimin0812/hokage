import { LanguageConfig } from './language-config'

export const translate = (key: string): string => {
    return LanguageConfig[key] ? LanguageConfig[key] : key
}