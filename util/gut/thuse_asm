; General Thing Use/Free        V0.01   June 1988  J.R.Oakley  QJUMP
;                                                   Tony Tebby
        section gen_util

        include 'dev8_keys_qdos_sms'

        xdef    gu_thuse
        xdef    gu_thfre

        xref    gu_thjmp

;+++
; Use a Thing, simple version.
;
;       Registers:
;               Entry                           Exit
;       A0      pointer to thing name           preserved
;       A1                                      pointer to Thing
;
;       Status return standard
;---
gu_thuse
ghu.reg reg     d1-d3/a2
        moveq   #sms.uthg,d0            ; we want to use
ghu_jump
        movem.l ghu.reg,-(sp)
        moveq   #sms.myjb,d1            ; for me
        moveq   #-1,d3                  ; wait for use
        jsr     gu_thjmp                ; do it
        movem.l (sp)+,ghu.reg
        rts

;+++
; Free a Thing, simple version, D0 preserved.
;
;       Registers:
;               Entry                           Exit
;       A0      pointer to thing name           preserved
;
;       Status return according to D0
;---
gu_thfre
        movem.l d0/a1,-(sp)              ; save regs
        moveq   #sms.fthg,d0             ; we want to free
        bsr.s   ghu_jump
        movem.l (sp)+,d0/a1
        tst.l   d0
        rts
        end
