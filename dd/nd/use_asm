* Set name of network file server  V0.0   1985  Tony Tebby
*
        section nd
*
        xdef    nfs_use
*
        xref    nu_io
        xref    ut_gtnm1
        xref    ut_fdev
*
        include dev8_dd_nd_keys
*
nfs_use
        moveq   #sv_ddlst,d3            look in dd list
        lea     nu_io(pc),a2
        bsr.l   ut_fdev                 for device driver
*
        bsr.l   ut_gtnm1                get the name that will be used
        bne.s   nfsu_rts
        move.w  (a6,a1.l),d7            length of name
        move.l  2(a6,a1.l),d6           and characters
        and.l   #$dfdfdfdf,d6           uppercase
        cmp.w   #4,d7                   more than four characters?
        bhi.s   nfsu_bn                 ... yes
*
        lea     ndd_name-ndd_ddlk(a0),a0  set pointer to name
        move.w  d7,(a0)+                and set new name
        move.l  d6,(a0)+
*
        moveq   #7,d7                   max 8 more parameters
nfsu_loop
        addq.l  #8,a3                   next parameter?
        cmp.l   a5,a3                   last?
        bge.s   nfsu_rts                ... yes
*
        bsr.l   ut_gtnm1                get the next name
        bne.s   nfsu_rts
        move.w  (a6,a1.l),d5            get the number of characters
        beq.s   nfsu_eloop              (null)
        subq.w  #3,d5                   take away Nx_
        ble.s   nfsu_bn                 ... oops
        cmp.w   #ndd.nuln-4,d5          no more than 28 left please
        bhi.s   nfsu_bn
        move.w  2(a6,a1.l),d4           starting with Nx
        bclr    #13,d4                  (n or N)?
        sub.w   #'N0',d4
        ble.s   nfsu_bn                 ... oops
        cmp.w   #8,d4                   device 1 to 8?
        bhi.s   nfsu_bn
        cmp.b   #'_',4(a6,a1.l)         'Nx_'?
        bne.s   nfsu_bn                 ... no
*
nfsu_set
        move.l  a0,a4
        move.w  d4,(a4)+                net node number
        move.w  d5,(a4)+                number of characters in use name
nfs_suse
        move.b  5(a6,a1.l),(a4)+        copy characters starting at 4th
        addq.l  #1,a1
        subq.w  #1,d5
        bgt.s   nfs_suse
nfsu_eloop
        add.w   #ndd.nuln,a0            move to next use name
        dbra    d7,nfsu_loop
        rts
*
nfsu_bn
        moveq   #err.bn,d0
nfsu_rts
        rts
        end
