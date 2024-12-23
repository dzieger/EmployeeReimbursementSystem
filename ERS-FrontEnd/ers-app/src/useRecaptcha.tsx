import { useCallback, useEffect, useRef, useState } from "react"
import ReCAPTCHA from "react-google-recaptcha"

const useRecaptcha = () => {
    const [captchaToken, setCaptchaToken] = useState<string>('')
    const recaptchaRef = useRef<ReCAPTCHA | null>(null)

    const handleRecaptcha = useCallback((token: string | null) => {
        setCaptchaToken(token || "")
    }, [])

    useEffect(() => {
        const refreshCaptcha = () => {
            if (recaptchaRef.current && captchaToken) {
                recaptchaRef.current.reset()
                setCaptchaToken("")
            }
        }

        let tokenRefreshTimeout: NodeJS.Timeout | null = null

        if (captchaToken) {
            tokenRefreshTimeout = setTimeout(refreshCaptcha, 1200000)
        }

        return () => {
            if (tokenRefreshTimeout) {
                clearTimeout(tokenRefreshTimeout)
            }
        }
    }, [captchaToken])

    return { captchaToken, setCaptchaToken, handleRecaptcha, recaptchaRef }
}

export default useRecaptcha