* File utilities                        v0.00   Apr 1988  J.R.Oakley  QJUMP
*
        section gen_util
*
        include 'dev8_qdos_io'
        include 'dev8_qdos_ioa'
        include 'dev8_qdos_sms'
*
        xdef    gu_fopen
        xdef    gu_fclos
        xdef    gu_trap3
*+++
* Open a file for the current Job in the manner required.
*
*       Registers:
*               Entry                           Exit
*       D0                                      error code
*       D3      access key                      preserved
*       A0      pointer to filename             channel ID
*---
gu_fopen
fopreg  reg     d1/a1 
        movem.l fopreg,-(sp)
        moveq   #myself,d1              ; for me
        moveq   #ioa.open,d0            ; open something
        trap    #do.ioa
        tst.l   d0
        movem.l (sp)+,fopreg
        rts
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
*+++
* Do file I/O with an infinite timeout, returning with condition codes set
*
*       Registers:
*               Entry                           Exit
*       D0      trap key                        error code
*       D1                                      ???
*       A0      Channel ID                      preserved
*       A1                                      ???
*---
gu_trap3
        move.l  d3,-(sp)
        moveq   #forever,d3
        trap    #do.io
        move.l  (sp)+,d3
        tst.l   d0
        rts
*
        end
