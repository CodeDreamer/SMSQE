; Procedure to start HOTKEY  V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_go

        xref    hot_thus
        xref    hot_thfr
        xref    hk_cjob
;+++
; Start the hotkey job
;---
hot_go
        jsr     hot_thus                 ; use thing
        bne.s   hg_rts
        jsr     hk_cjob                  ; create job
        jmp     hot_thfr                 ; free thing
hg_rts
        rts
        end
