* File utilities                        v0.00   Apr 1988  J.R.Oakley  QJUMP
*
        section gen_util
*
        include 'dev8_keys_qdos_ioa'
        include 'dev8_keys_qdos_sms'
*
        xdef    gu_fopen
*+++
* Open a file for the current Job in the manner required.
*
*       Registers:
*               Entry                           Exit
*       D0                                      error code
*       D3      access key                      preserved
*       A0      pointer to filename             channel ID -1 if not open
*---
gu_fopen
fopreg  reg     d1/a1 
        movem.l fopreg,-(sp)
        moveq   #myself,d1              ; for me
        moveq   #ioa.open,d0            ; open something
        trap    #do.ioa
        tst.l   d0
        beq.s   guf_exit
        move.w  #-1,a0
guf_exit
        movem.l (sp)+,fopreg
        rts
*
        end
