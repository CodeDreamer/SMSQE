; Hotkey Use/Free Linkage Block          V0.01   June 1988  J.R.Oakley  QJUMP

        section gen_util

        include 'dev8_keys_qdos_sms'

        xdef    gu_hkuse
        xdef    gu_hkfre

        xref    gu_thjmp

;+++
; Find and use the hotkey linkage block: this is an exclusive Thing, so the
; code must release it when finished, and preferably fairly soon.
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       A3                                      linkage block
;---
gu_hkuse
ghu.reg reg     d1-d3/a0-a2
        moveq   #sms.uthg,d0            ; we want to use
ghu_jump
        movem.l ghu.reg,-(sp)
        moveq   #sms.myjb,d1            ; for me
        moveq   #127,d3                 ; wait for use
        lea     hk_thnam,a0             ; name of thing
        jsr     gu_thjmp                ; do it
        move.l  a1,a3
        movem.l (sp)+,ghu.reg
        tst.l   d0
        rts

;+++
; Free the hotkey linkage block.
;
;       Registers:
;               Entry                           Exit
;       D0,CCR  error code                      preserved
;---
gu_hkfre
        movem.l d0/a3,-(sp)              ; save regs
        moveq   #sms.fthg,d0             ; we want to free
        bsr.s   ghu_jump
        movem.l (sp)+,d0/a3
        tst.l   d0
        rts

hk_thnam dc.w   6,'Hotkey'

        end
