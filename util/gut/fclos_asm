* File utilities                        v0.00   Apr 1988  J.R.Oakley  QJUMP
*
        section gen_util
*
        include 'dev8_keys_qdos_ioa'
*
        xdef    gu_fclos
*+++
* Close a file, preserving all registers and returning the CCR corresponding
* to the value of D0.
*
*       Registers:
*               Entry                           Exit
*       A0      channel ID                      preserved
*---
gu_fclos
fclreg  reg     d0/a0/a1 
        movem.l fclreg,-(sp)
        moveq   #ioa.clos,d0
        trap    #do.ioa
        movem.l (sp)+,fclreg
        tst.l   d0
        rts
*
        end

