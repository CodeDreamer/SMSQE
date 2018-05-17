* Spooler default  V0.8    Tony Tebby   QJUMP
*
*       SPL_USE name                   set default destination for spooler
*
        section exten 
*
        xdef    spl_use
*
        xref    any_use
*
        include dev8_sbsext_ext_keys
*
spl_use
        moveq   #sv_spld-sv_progd+$70,d4 set address of spooler default
        bra.s   any_use
        end
