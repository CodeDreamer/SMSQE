* General heap utilities                v0.00   Apr 1988  J.R.Oakley  QJUMP
*
        section gen_util
*
        include 'dev8_keys_qdos_sms'
*
        xdef    gu_rchp
*+++
* Return a piece of heap: all registers are preserved, and the condition
* codes are set according to D0.
*
*       Registers:
*               Entry                           Exit
*       A0      heap to return                  preserved
*---
gu_rchp
hprreg  reg     d0-d3/a0-a3
        movem.l hprreg,-(sp)
        moveq   #sms.rchp,d0
        trap    #do.sms2
        movem.l (sp)+,hprreg
        tst.l   d0
        rts
*
        end
