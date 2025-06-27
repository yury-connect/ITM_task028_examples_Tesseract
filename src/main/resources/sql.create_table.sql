CREATE TABLE recognized_text (
                                 id UUID PRIMARY KEY,
                                 rt_info TEXT,
                                 rt_text TEXT,
                                 rt_date TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP(0) NOT NULL
);