; Procedure to stop HOTKEY  V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_stop

        xref    hot_thus
        xref    hot_thfr
        xref    hk_kjob
;+++
; Stop the hotkey job
;---
hot_stop
        jsr     hot_thus                 ; use thing
        bne.s   hg_rts
        jsr     hk_kjob                  ; kill job
        jmp     hot_thfr                 ; free thing
hg_rts
        rts
        end
