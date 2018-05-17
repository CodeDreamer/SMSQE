; Function to remove HOTKEY   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_remv

        xref    hot_thar
        xref    hot_rter

        xref    hk_remv

        xref    ut_gxnm1

;+++
; SuperBASIC Remove hotkey item
; error = HOT_REMV (key or name)
;---
hot_remv
        jsr     ut_gxnm1                 ; get name of thing to remove
        bne.s   hr_rts

        lea     hk_remv,a2               ; remove
        jsr     hot_thar                 ; (a1 relative a6)
        jmp     hot_rter                 ; return error

hr_rts
        rts
        end
