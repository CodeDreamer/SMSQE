*       Priority setting utilities
*
        section action
*
        include 'dev8_ptr_keys'
        include 'dev8_qdos_sms'
*
        xdef    qr_lopri
        xdef    qr_hipri
*
prireg  reg     d0-d2/a0
*+++
* Set the current job to a priority of 4: all registers are preserved.
*---
qr_lopri
        movem.l prireg,-(sp)
        moveq   #4,d2
        bra.s   pri_ent
*+++
* Set the current job to a priority of 127: all registers are preserved.
*---
qr_hipri
        movem.l prireg,-(sp)
        moveq   #127,d2
pri_ent
        moveq   #myself,d1
        moveq   #sms.spjb,d0
        trap    #do.sms2
        movem.l (sp)+,prireg
        rts
*
        end
