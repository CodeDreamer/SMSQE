* Find a device driver linkage block    1985  Tony Tebby   QJUMP
*
        section utils
*
        xdef    ut_fdev
*
        include dev8_sbsext_ext_keys
*
*       d0    s
*       d1    s
*       d2    s
*       d3 c    sysvar offset of IOD link list or DD link list
*       a0   r  pointer to iod or dd linkage address
*       a2 c    pointer to IO routine
*
ut_fdev
        moveq   #mt.inf,d0              find the sysvars
        trap    #1
        move.l  (a0,d3.w),a0            set the iod linkage address
*
fdev_look
        cmp.l   iod_iolk-iod_ddlk(a0),a2 the right driver?
        beq.s   fdev_rts                ... yes
        move.l  (a0),a0                 ... no, try the next
        move.l  a0,d0                   is this the last?
        bne.s   fdev_look               ... no
        moveq   #err.nf,d0              ... yes, not found
*
fdev_rts
        rts
        end
