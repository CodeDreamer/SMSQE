; General heap allocatiion           v0.01   Apr 1988  J.R.Oakley  QJUMP

        section gen_util

        include 'dev8_keys_qdos_sms'

        xdef    gu_achp0
        xdef    gu_achpj
        xdef    gu_achpp

;+++
; Allocate heap permanently, preserving register contents.
;
;       Registers:
;               Entry                           Exit
;       D0      space required                  error code
;       A0                                      space allocated
;---
gu_achpp
hpareg  reg     d1-d3/a1-a3
        movem.l hpareg,-(sp)
        moveq   #0,d2                    ; permanently
        bra.s   uga_achp
;+++
; Allocate heap for Job, preserving register contents.
;
;       Registers:
;               Entry                           Exit
;       D0      space required                  error code
;       A0      JOB ID                          space allocated
;---
gu_achpj
        movem.l hpareg,-(sp)
        move.l  a0,d2                    ; for Job
        bra.s   uga_achp
;+++
; Allocate heap for current job, preserving register contents.
;
;       Registers:
;               Entry                           Exit
;       D0      space required                  error code
;       A0                                      space allocated
;---
gu_achp0
        movem.l hpareg,-(sp)
        moveq   #sms.myjb,d2             ; for me
uga_achp
        move.l  d0,d1                    ; this much space
        moveq   #sms.achp,d0             ; allocate
        trap    #do.sms2
        tst.l   d0
        movem.l (sp)+,hpareg
        rts

        end
