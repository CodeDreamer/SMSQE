; Conditional Heap allocation           v0.01   Apr 1988  J.R.Oakley  QJUMP

        section gen_util

        include 'dev8_keys_qdos_sms'
        include 'dev8_keys_sys'

        xdef    gu_achp2

;+++
; Allocate heap for current job, preserving register contents. If there is
; not at least twice as much room as required, give up
;
;       Registers:
;               Entry                           Exit
;       D0      space required                  negative if not possible
;       A0                                      space allocated
;---
gu_achp2
ga2.reg reg     d1/d2/d3/a1/a2/a3
        movem.l ga2.reg,-(sp)
        move.l  d0,d3
        moveq   #sms.info,d0
        trap    #do.sms2                 ; get information

        move.l  sys_sbab(a0),d0
        sub.l   sys_fsbb(a0),d0
        sub.l   #$400,d0
        asr.l   #1,d0                    ; half available
        sub.l   d3,d0                    ; enough?
        blt.s   ga2_exit

        moveq   #sms.myjb,d2             ; for me
        move.l  d3,d1                    ; this much space
        moveq   #sms.achp,d0             ; allocate
        trap    #do.sms2
        tst.l   d0
ga2_exit
        movem.l (sp)+,ga2.reg
        rts

        end
